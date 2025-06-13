'use client'

import { Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer, Tooltip, Legend } from 'recharts';
const data = [
  {
    subject: 'Math',
    A: 120,
    B: 110,
    fullMark: 150,
  },
  {
    subject: 'Chinese',
    A: 98,
    B: 130,
    fullMark: 150,
  },
  {
    subject: 'English',
    A: 86,
    B: 130,
    fullMark: 150,
  },
  {
    subject: 'Geography',
    A: 99,
    B: 100,
    fullMark: 150,
  },
  {
    subject: 'Physics',
    A: 85,
    B: 90,
    fullMark: 150,
  },
  {
    subject: 'History',
    A: 65,
    B: 85,
    fullMark: 150,
  },
];
import React from 'react'
import { RadarData } from '@/app/supervisor/superVizorDataTypes/types';

export const RadarChartt = ({radarData} : { radarData: RadarData | undefined}) => {

const prettyLabels = {
  GivenTasksAfterDeadline: "Given Tasks Solved After Deadline Ratio",
  GivenTasksBeforeDeadline: "Given Tasks Solved Before Deadline Ratio",
  GivenTasksFinishedTask: "Given Tasks Finished Task Ratıo",
  GivenTaskFinalStatus: "Given Tasks Final Status Actıve Ratio"
};

const fullMarks = {
  GivenTasksAfterDeadline: 100,
  GivenTasksBeforeDeadline: 100,
  GivenTasksFinishedTask: 100,
  GivenTaskFinalStatus: 100
};

const transformed = radarData?.map(obj => {
  const [key, value] = Object.entries(obj)[0];
  const full = fullMarks[key] || 100;
  return {
    subject: prettyLabels[key] || key,
    value: Math.round((value / full) * 100),
    fullMark: 100
  };
});
   console.log(transformed);
    return (
      <ResponsiveContainer width="100%" height="100%">
<RadarChart cx="50%" cy="50%" outerRadius="80%" data={transformed}>
  <PolarGrid />
  <PolarAngleAxis dataKey="subject" />
  <PolarRadiusAxis angle={30} domain={[0, 100]} />
  <Radar name="Supervisor Stats" dataKey="value" stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
</RadarChart>
      </ResponsiveContainer>
    );
}

