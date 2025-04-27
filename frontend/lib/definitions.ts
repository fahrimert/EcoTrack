import { z } from "zod";

export const SignİnFormSchema = z.object({
  email: z.string().email({ message: "Geçerli bir email giriniz." }),
  name: z.string().min(2,{ message: "İsminiz için en az 2 karakter giriniz doğru giriniz" }),
  password: z
    .string()
    .min(8, { message: "En Az 8 Karakter, " })
    .regex(/[a-zA-Z]/, { message: "en az bi harf ," })
    .regex(/[0-9]/, { message:"en az bir sayı ,"  })
});


export const createLocationFormSchema = z.object({
  name: z.string().min(3, {
    message: "Konum  en az 3 karakter olmalıdır.",
      }),
    placeId: z.string().optional(), // Google Places placeId'si
    lat: z.number().optional(), // Enlem
    lng: z.number().optional(), // Boylam


});


export type FormState =
  | {
      errors?: {
        email?: string[];
        name?:string[]
        password?: string[];
      };
      message?: string;
    }
  | undefined;
  export type LocationFormState =
  | {
      errors?: {
        name?: string[];
        placeId?:string[]
        lat?: string[];
        latlng?: string[];
      };
      message?: string;
    }
  | undefined;