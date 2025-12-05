"use client";

import React from "react";
import { AppRouterCacheProvider } from '@mui/material-nextjs/v15-appRouter';
import { Provider as ChakraProvider } from "@/components/ui/provider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "react-hot-toast";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000, 
      retry: 1, 
    },
  },
});

export default function AppProviders({ children }: { children: React.ReactNode }) {
  return (
    <QueryClientProvider client={queryClient}>
      <AppRouterCacheProvider>
          <ChakraProvider>
      <Toaster position="top-center" reverseOrder={false} />
              {children}
          </ChakraProvider>
      </AppRouterCacheProvider>
    </QueryClientProvider>
  );
}