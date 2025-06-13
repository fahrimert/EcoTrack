import { createContext } from "react";

export const HeatMapSelectWorkerContext = createContext<{
  usernameForHeatmap: number | null;
  setUsernameForHeatmap: (value: number) => void;
}>({
  usernameForHeatmap: null,
  setUsernameForHeatmap: () => {},
});
