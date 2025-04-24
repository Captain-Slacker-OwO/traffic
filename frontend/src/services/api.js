import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NODE_ENV === 'development' ? 
    'http://localhost:8080/api' : '/api',
  timeout: 5000
});

export const fetchTrafficData = async (intersection, date) => {
  try {
    const response = await api.get(`/traffic/history`, {
      params: {
        intersection,
        date: date.toISOString().split('T')[0],
        page: 0,
        size: 24
      }
    });
    return response.data;
  } catch (error) {
    console.error('请求失败:', error); // 替换未定义的showErrorToast
    throw error;
  }
};