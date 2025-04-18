

/** @type {import('next').NextConfig} */
const nextConfig = {
    
  experimental: {
      missingSuspenseWithCSRBailout: false,
    },
  images : {
      domains: ['res.cloudinary.com'],
  
     },
};

export default nextConfig;
