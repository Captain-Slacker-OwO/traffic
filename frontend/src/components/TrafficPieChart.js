import React from 'react';
import { Pie } from 'react-chartjs-2';

export default function TrafficPieChart({ data }) {
    const chartData = {
        labels: ['畅通', '缓行', '拥堵'],
        datasets: [{
            data: [data.free, data.slow, data.congested],
            backgroundColor: [
                '#4CAF50',
                '#FFC107',
                '#F44336'
            ]
        }]
    };

    return <Pie data={chartData} />;
}