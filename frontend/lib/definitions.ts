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
export const RegisterFormSchema = z.object({
  email: z.string().email({ message: "Geçerli Bir Email giriniz." }).endsWith("edu.tr","Sadece öğrenci emailleri geçerli"),
  name: z.string().min(2, { message: "Geçerli Bir İsim giriniz." }),
  university:z.string().min(2,{message:"Üniversite seçiniz"}),
  major:z.string().min(2,{message:"Bölüm seçiniz"}).min(2,{message:"Bölüm Seçiniz"}),
  password: z
    .string()
    .min(8, { message: "En Az 8 Karakter, " })
    .regex(/[a-zA-Z]/, { message: "En az bi harf ," })
    .regex(/[0-9]/, { message: "En Az Bir Sayı ," })
    .regex(/[^a-zA-Z0-9]/, {
      message: "ve En az bir özel karakter giriniz..",
    }),
});

export const resetPasswordScMEA = z.object({
  email: z.string().email({ message: "Geçerli Bir Email giriniz." }).endsWith(".edu.tr","Sadece öğrenci emailleri geçerli"),
  password: z
    .string()
    .min(8, { message: "En az 8 karakter , " })
    .regex(/[a-zA-Z]/, { message: "en az bi harf ," })
    .regex(/[0-9]/, { message: "en az bir sayı ," })
    .regex(/[^a-zA-Z0-9]/, {
      message: "ve en az bir özel karakter giriniz..",
    }),
  resetPassword: z
    .string()
    .min(8, { message: "En az 8 karakter ," })
    .regex(/[a-zA-Z]/, { message: "en az bi harf ," })
    .regex(/[0-9]/, { message: "en az bir sayı ," })
    .regex(/[^a-zA-Z0-9]/, {
      message: "ve en az bir özel karakter giriniz doğrulamak için",
    }),
});
export const ResetPasswordSchema = z.object({
  resetEmail: z.string().email().min(2, {
    message: "Email en az 2 karkater olmalı .",
  }).endsWith(".edu.tr","Sadece öğrenci emailleri geçerli"),
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
export type RegisterFormState =
  | {
      errors?: {
        email?: string[];
        name?: string[];
        university?:string[];
        major?:string[];
        password?: string[];
      };
      message?: string;
    }
  | undefined;
export type ResetPasswordFormState =
  | {
      errors?: {
        resetEmail?: string[];
      };
      message?: string;
    }
  | undefined;

export type ResetPasswordFormStatee =
  | {
      errors?: {
        email?: string[];
        password?: string[];
        resetPassword?: string[];
      };
      message?: string;
    }
  | undefined;
