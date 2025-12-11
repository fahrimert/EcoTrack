"use client"
import axios from "axios";
import { cookies } from "next/headers"; 

export const getClientApi =  () => {
const token = localStorage.getItem("token");
  const api = axios.create({
    baseURL: "http://localhost:8080",
  });

if (token) api.defaults.headers.Authorization = `Bearer ${token}`; 

  return api;
};