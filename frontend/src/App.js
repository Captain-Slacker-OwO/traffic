import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TrafficChart from './components/TrafficChart';
import './App.css';

function HomePage() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>交通流量分析系统</h1>
        <div className="author-info">
          <p>作者：夏宇</p>
          <p>学校：长沙理工大学 计算机学院</p>
          <p>项目特点：基于AI的实时交通流量预测系统</p>
        </div>
        <div className="action-buttons">
          <a href="/traffic" className="nav-button">查看交通数据</a>
          <a href="https://github.com/your-repo" className="nav-button" target="_blank">项目源码</a>
        </div>
      </header>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/traffic" element={<TrafficChart intersection="1" />} />
      </Routes>
    </Router>
  );
}

export default App;
