import React from 'react';

export default function TimeRangeSelector({ value, onChange }) {
  return (
    <select 
      value={value} 
      onChange={(e) => onChange(e.target.value)}
      className="time-range-selector"
    >
      <option value="last24hours">最近24小时</option>
      <option value="last7days">最近7天</option>
      <option value="last30days">最近30天</option>
    </select>
  );
}