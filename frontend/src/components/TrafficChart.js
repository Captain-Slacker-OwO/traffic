import React, { useEffect, useState } from 'react';
import { Line } from 'react-chartjs-2';
import { fetchTrafficData } from '../services/api';

const TrafficChart = ({ intersection }) => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [data, setData] = useState([]);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        const response = await fetchTrafficData(intersection, new Date());
        // 确保正确处理响应数据结构
        const responseData = response.data || response;
        if (Array.isArray(responseData)) {
          setData(responseData);
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
    
    loadData();
    const interval = setInterval(loadData, 3000);
    return () => clearInterval(interval);
  }, [intersection]);

  if (loading) {
    return <div className="data-loading">数据加载中...</div>;
  }

  if (error) {
    return <div className="error-message">加载失败: {error}</div>;
  }

  const chartData = {
    labels: data.map(item => new Date(item.timestamp).toLocaleTimeString()),
    datasets: [{
      label: '实时交通流量',
      data: data.map(item => item.vehicleCount),
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.1
    }]
  };

  const options = {
    responsive: true,
    plugins: {
      title: { display: true, text: `${intersection}号路口交通流量` }
    },
    scales: {
      y: { beginAtZero: true, title: { display: true, text: '车辆数' } },
      x: { title: { display: true, text: '时间' } }
    }
  };

  if (data.length === 0) {
    return <div className="data-loading">数据加载中...</div>;
  }

  try {
    // 原图表渲染代码
  } catch (error) {
    return <div className="error-message">图表渲染失败：{error.message}</div>;
  }
  return <Line data={chartData} options={options} />;
};

export default TrafficChart;  // Add this line