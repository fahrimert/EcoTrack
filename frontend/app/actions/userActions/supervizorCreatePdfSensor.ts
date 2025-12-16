"use server"

import axios from "axios";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";

export async function supervizorCreatePdfSensor(formData: FormData,session:RequestCookie | undefined){
try {

          await axios.post("http://localhost:8080/supervizor/createPdfReportAndSendToManager",formData,
        
              {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${session?.value}`,
    
              },
            }  )

                   return {
            serverData: "İlgili Müdüre Pdf Gönderildi",
          }; 
        
      

} catch (error  : any) {
     if (axios.isAxiosError(error) && error.response) {
    return {
      serverError: error.response.data || "Bir hata oluştu.",
    };
  } else {
    return {
      serverError: "Beklenmeyen bir hata oluştu.",
    };
  }
}

}