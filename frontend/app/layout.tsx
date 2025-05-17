import { Provider } from "@/components/ui/provider";
import "./globals.css";
import Providers from "./providers/Providers";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="w-full ">
        <Provider>
        <Providers>{children}</Providers>
        </Provider>

      </body>
    </html>
  );
}
