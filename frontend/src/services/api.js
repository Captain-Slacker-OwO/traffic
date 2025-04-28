import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NODE_ENV === 'development' ? 
    'http://localhost:8080/api' : '/api',
  timeout: 5000
});

export const fetchTrafficData = async (intersection, timeRange) => {
  try {
    const params = { 
      intersectionId: intersection,
      timeRange: timeRange  // 统一使用timeRange参数
    };
    
    const response = await api.get(`/traffic/history`, { params });
    return response.data;
  } catch (error) {
    console.error('请求失败:', error);
    throw error;
  }
};