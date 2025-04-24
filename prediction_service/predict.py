from flask import Flask, request, jsonify
import pandas as pd
from fbprophet import Prophet
import pymysql
import os
from apscheduler.schedulers.background import BackgroundScheduler

app = Flask(__name__)
model = None
MODEL_VERSION = 1.0
LAST_TRAIN_DATE = None
MODEL_SAVE_PATH = 'models/'
os.makedirs(MODEL_SAVE_PATH, exist_ok=True)

def load_data(intersection):
    host = os.getenv('DB_HOST', 'localhost')
    conn = pymysql.connect(host=host, user='root', password='your_password',
                           database='traffic_db', charset='utf8')
    try:
        with conn.cursor() as cursor:
            sql = """SELECT timestamp AS ds, COUNT(*) AS y 
                     FROM traffic_record 
                     WHERE intersection_id=%s 
                     GROUP BY DATE_FORMAT(timestamp, '%Y-%m-%d %H')"""
            cursor.execute(sql, (intersection,))
            df = pd.DataFrame(cursor.fetchall(), columns=['ds', 'y'])
        return df
    finally:
        conn.close()

def scheduled_retraining():
    global MODEL_VERSION, LAST_TRAIN_DATE
    # 训练逻辑
    df = load_data(intersection)
    model = Prophet()
    model.fit(df)
    MODEL_VERSION += 1
    model.save(f"{MODEL_SAVE_PATH}model_{MODEL_VERSION}.pkl")
    LAST_TRAIN_DATE = pd.Timestamp.now()

scheduler = BackgroundScheduler()
scheduler.add_job(scheduled_retraining, 'interval', days=1)
scheduler.start()

@app.route('/predict')
def predict():
    global model
    intersection = request.args.get('intersection')
    
    if not intersection:
        return jsonify({'error': 'Missing intersection parameter'}), 400

    try:
        if model is None:
            model = Prophet()
            df_train = load_data(intersection)
            model.fit(df_train)
        
        future = model.make_future_dataframe(periods=1, freq='H')
        forecast = model.predict(future)
        next_hour = forecast.tail(1)[['ds', 'yhat']].iloc[0]
        
        return jsonify({
            'intersection': intersection,
            'ds': str(next_hour['ds']),
            'yhat': float(next_hour['yhat'])
        })
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)