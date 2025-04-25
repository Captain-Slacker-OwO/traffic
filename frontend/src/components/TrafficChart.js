import React, { useEffect, useState } from 'react';
import { Line, Pie } from 'react-chartjs-2';
import { fetchTrafficData } from '../services/api';

const TrafficChart = ({ intersection }) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [data, setData] = useState([]);
  const [lastUpdated, setLastUpdated] = useState(null);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await fetchTrafficData(intersection, new Date());
      const responseData = response.data || response;
      if (Array.isArray(responseData)) {
        setData(responseData);
        setLastUpdated(new Date());
      } else {
        throw new Error('Invalid data format');
      }
    } catch (err) {
      console.error('数据加载失败:', err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    const interval = setInterval(loadData, 30000); // 30秒刷新一次
    return () => clearInterval(interval);
  }, [intersection]);

  if (loading) {
    return (
      <div className="traffic-container">
        <div className="data-loading">
          <p>正在加载数据...</p>
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="traffic-container">
        <div className="error-message">
          <p>加载失败: {error}</p>
          <button className="refresh-button" onClick={loadData}>
            重试
          </button>
        </div>
      </div>
    );
  }

  // 计算交通状态统计数据
  const trafficStatus = {
    free: data.filter(item => item.status === 'free').length,
    slow: data.filter(item => item.status === 'slow').length,
    congested: data.filter(item => item.status === 'congested').length
  };

  const lineChartData = {
    labels: data.map(item => new Date(item.timestamp).toLocaleTimeString()),
    datasets: [
      {
        label: '实时交通流量',
        data: data.map(item => item.vehicleCount),
        borderColor: '#3498db',
        backgroundColor: 'rgba(52, 152, 219, 0.1)',
        borderWidth: 2,
        tension: 0.4
      },
      {
        label: '预测流量',
        data: data.map(item => item.predictedCount || null),
        borderColor: '#e74c3c',
        backgroundColor: 'rgba(231, 76, 60, 0.1)',
        borderWidth: 2,
        borderDash: [5, 5],
        tension: 0.4
      }
    ]
  };

  const pieChartData = {
    labels: ['畅通', '缓行', '拥堵'],
    datasets: [{
      data: [trafficStatus.free, trafficStatus.slow, trafficStatus.congested],
      backgroundColor: ['#2ecc71', '#f39c12', '#e74c3c']
    }]
  };

  return (
    <div className="traffic-container">
      <div className="chart-header">
        <h2 className="chart-title">{intersection}号路口交通流量分析</h2>
        <div>
          <span style={{marginRight: '1rem'}}>
            最后更新: {lastUpdated?.toLocaleTimeString()}
          </span>
          <button className="refresh-button" onClick={loadData}>
            刷新数据
          </button>
        </div>
      </div>

      <div style={{marginBottom: '2rem'}}>
        <Line 
          data={lineChartData} 
          options={{
            responsive: true,
            plugins: {
              legend: { position: 'top' },
              tooltip: { mode: 'index', intersect: false }
            },
            scales: {
              y: { 
                beginAtZero: true,
                title: { display: true, text: '车辆数' }
              },
              x: { 
                title: { display: true, text: '时间' },
                grid: { display: false }
              }
            }
          }}
        />
      </div>

      <div style={{maxWidth: '300px', margin: '0 auto'}}>
        <h3 style={{textAlign: 'center', marginBottom: '1rem'}}>交通状态分布</h3>
        <Pie data={pieChartData} />
      </div>
    </div>
  );
};

export default TrafficChart;