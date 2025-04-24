document.addEventListener('DOMContentLoaded', function() {
  const chart = echarts.init(document.getElementById('trafficChart'));
  const option = {
    title: {
      text: '实时交通流量'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: Array.from({length:24}, (_,i) => i + ':00')
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: Array(24).fill().map(() => Math.floor(Math.random()*200)),
      type: 'line',
      smooth: true
    }],
    dataZoom: [{
      type: 'slider',
      start: 0,
      end: 100
    }]
  };
  chart.setOption(option);
  window.addEventListener('resize', function() {
    chart.resize();
  });
  const socket = new WebSocket('ws://localhost:8080/ws/traffic');
  socket.onmessage = function(event) {
    const newData = JSON.parse(event.data);
    const data = option.series[0].data;
    data.push(newData);
    data.shift();
    option.xAxis.data.push(new Date().toLocaleTimeString());
    option.xAxis.data.shift();
    chart.setOption({
      series: [{
        data: data
      }],
      xAxis: {
        data: option.xAxis.data
      }
    });
  };
});