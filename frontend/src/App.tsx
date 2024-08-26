import { AuthProvider } from "./providers/AuthProvider";
import { SnackbarProvider } from "./providers/SnackbarProvider";
import { Routing } from "./router/Routing";
import { AxiosInterceptor } from "./utils/apiClient";
import React from "react";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";

function App() {
  return (
    <GoogleReCaptchaProvider
      reCaptchaKey={process.env.REACT_APP_RECAPTCHA_SITE_KEY || ""}
    >
      <SnackbarProvider>
        <AxiosInterceptor>
          <AuthProvider>
            <Routing />
          </AuthProvider>
        </AxiosInterceptor>
      </SnackbarProvider>
    </GoogleReCaptchaProvider>
  );
}

export default App;
