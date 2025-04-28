import React, { useEffect, useState, useCallback } from 'react';
import { Line, Pie } from 'react-chartjs-2';
import { fetchTrafficData } from '../services/api';
import { 
  Chart,
  LinearScale,
  CategoryScale,
  PointElement,
  LineElement,
  LineController,
  ArcElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';
import TimeRangeSelector from './TimeRangeSelector';

Chart.register(
  LinearScale,
  CategoryScale,
  PointElement,
  LineElement,
  LineController,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

const TrafficChart = ({ intersection }) => {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState(null);
  const [data, setData] = useState([]);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [timeRange, setTimeRange] = useState('last7days');

  const loadData = useCallback(async (isRefresh = false) => {
    try {
      if (isRefresh) {
        setRefreshing(true);
      } else {
        setLoading(true);
      }
      setError(null);
      const response = await fetchTrafficData(intersection, timeRange);
      const responseData = response.data;
      if (Array.isArray(responseData)) {
        setData(responseData);
      } else {
        throw new Error('无效数据格式: 期望数组格式');
      }
      setLastUpdated(new Date());
    } catch (err) {
      console.error('数据加载失败:', err);
      setError(err.message);
    } finally {
      if (isRefresh) {
        setRefreshing(false);
      } else {
        setLoading(false);
      }
    }
  }, [intersection, timeRange]);

  useEffect(() => {
    loadData();
    const interval = setInterval(() => loadData(true), 30000);
    return () => clearInterval(interval);
  }, [loadData]);

  const trafficStatus = {
    free: data.filter(item => item.trafficStatus === 'free').length,
    slow: data.filter(item => item.trafficStatus === 'slow').length,
    congested: data.filter(item => item.trafficStatus === 'congested').length
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
      {/* 顶部区域 */}
      <div className="chart-header">
        <TimeRangeSelector 
          value={timeRange}
          onChange={setTimeRange}
        />
        <h2 className="chart-title">{intersection}号路口交通流量分析</h2>
        <div>
          <button 
            className="refresh-button" 
            onClick={async () => {
              await fetch('/api/traffic/generate?count=100', { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                  if (data.success) {
                    alert('已生成100条测试数据');
                    loadData(true);
                  }
                });
            }}
            style={{ marginRight: '1rem' }}
          >
            生成测试数据
          </button>
          <span style={{ marginRight: '1rem' }}>
            最后更新: {lastUpdated ? lastUpdated.toLocaleTimeString() : '暂无'}
          </span>
          <button className="refresh-button" onClick={() => loadData(true)} disabled={refreshing}>
            {refreshing ? '刷新中...' : '刷新数据'}
          </button>
        </div>
      </div>

      {/* 中间内容区域 */}
      <div style={{ marginBottom: '2rem', minHeight: '400px', position: 'relative' }}>
        {loading ? (
          <div className="skeleton-loader">
            {/* 骨架屏动画 */}
            <div className="skeleton-chart"></div>
          </div>
        ) : error ? (
          <div className="error-message">
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>⚠️</div>
            <h3 style={{ color: '#e74c3c' }}>数据加载失败</h3>
            <p style={{ color: '#666' }}>{error}</p>
          </div>
        ) : (
          <Line 
            data={lineChartData}
            options={{
              responsive: true,
              animation: { duration: 500 },
              plugins: {
                legend: { position: 'top' },
                tooltip: { mode: 'index', intersect: false }
              },
              scales: {
                y: {
                  type: 'linear',
                  beginAtZero: true,
                  title: { display: true, text: '车辆数' }
                },
                x: {
                  type: 'category',
                  title: { display: true, text: '时间' },
                  grid: { display: false }
                }
              }
            }}
          />
        )}
      </div>

      {/* 底部饼图区域 */}
      {!loading && !error && (
        <div style={{ maxWidth: '300px', margin: '0 auto', transition: 'all 0.5s ease' }}>
          <h3 style={{ textAlign: 'center', marginBottom: '1rem' }}>交通状态分布</h3>
          <Pie 
            key={intersection}
            data={pieChartData}
            options={{
              plugins: {
                legend: { position: 'bottom' }
              }
            }}
          />
        </div>
      )}
    </div>
  );
};

export default TrafficChart;
