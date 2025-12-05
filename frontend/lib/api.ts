import axios from "axios";
import { cookies } from "next/headers"; 

export const getServerApi = () => {
  const cookieStore = cookies();
  const token = cookieStore.get("session")?.value;

  const api = axios.create({
    baseURL: "http://localhost:8080",
  });

  if (token) {
    api.defaults.headers.Authorization = `Bearer ${token}`;
  }

  return api;
};