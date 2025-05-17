import { createContext } from "react";
    var today = new Date().getDate();

export  const DateContext = createContext({
    sortDate:today,
})