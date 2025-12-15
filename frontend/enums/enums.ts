export enum SensorStatus {
  ACTIVE = 'ACTIVE',
  FAULTY = 'FAULTY',
  IN_REPAIR = 'IN_REPAIR',
  SOLVED = 'SOLVED',
}

export const SensorStatusConfig: Record<SensorStatus, { label: string; color: string }> = {
  [SensorStatus.ACTIVE]: {
    label: 'Active',
    color: '#2bf21e',
  },
  [SensorStatus.FAULTY]: {
    label: 'Faulty',
    color: '#d63124',
  },
  [SensorStatus.IN_REPAIR]: {
    label: 'In Repair',
    color: '#e8d14d',
  },
  [SensorStatus.SOLVED]: {
    label: 'Solved',
    color: '#e8d14d', 
  },
};