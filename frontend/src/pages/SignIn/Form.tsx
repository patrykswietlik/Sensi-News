import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  TextField,
} from "@mui/material";
import { Field, Form, Formik, FormikHelpers } from "formik";
import { useCallback, useContext, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { NavLink } from "../../components/atoms/Link";
import { getUser, signIn } from "../../requests/auth";
import { RoutePath } from "../../router/RoutePath ";
import { color } from "../../styles/colors";
import {
  initialValues,
  getValidationSchema,
  SignInFormValues,
} from "./validationSchema";
import { AuthContext } from "../../providers/AuthProvider";
import { GoogleReCaptcha } from "react-google-recaptcha-v3";

export const SignInForm = () => {
  const { t } = useTranslation();
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const { setLoggedUser } = useContext(AuthContext);
  const [token, setToken] = useState("");
  const [refreshReCaptcha, setRefreshReCaptcha] = useState(false);

  const handleSignIn = async (
    values: SignInFormValues,
    { setSubmitting }: FormikHelpers<SignInFormValues>,
  ) => {
    if (!token) return;

    await signIn(values, token);
    await getUser().then((user) => {
      setLoggedUser(user);
    });
    setSubmitting(false);
    setRefreshReCaptcha((r) => !r);
    navigate(RoutePath.START);
  };

  const handleVerifyReCaptcha = useCallback((token: string) => {
    setToken(token);
  }, []);

  return (
    <>
      <Formik
        initialValues={initialValues}
        validationSchema={getValidationSchema()}
        onSubmit={handleSignIn}
      >
        {({ errors, touched, isSubmitting }) => (
          <Form>
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                width: "100%",
                maxWidth: "400px",
                minWidth: { xs: "auto", md: "400px" },
              }}
            >
              <Box sx={{ width: "100%", padding: "5px" }}>
                <Field
                  as={TextField}
                  name="email"
                  type="email"
                  label="Email"
                  variant="standard"
                  fullWidth
                  error={touched.email && !!errors.email}
                  helperText={touched.email && errors.email}
                  InputProps={{
                    style: { fontSize: "18px" },
                  }}
                  InputLabelProps={{
                    style: { fontSize: "18px" },
                  }}
                />
              </Box>
              <Box sx={{ width: "100%", padding: "10px" }}>
                <Field
                  as={TextField}
                  name="password"
                  type={showPassword ? "text" : "password"}
                  label={t("signIn.password.label")}
                  variant="standard"
                  fullWidth
                  error={touched.password && !!errors.password}
                  helperText={touched.password && errors.password}
                  InputProps={{
                    style: { fontSize: "18px" },
                  }}
                  InputLabelProps={{
                    style: { fontSize: "18px" },
                  }}
                />
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={showPassword}
                      onChange={() => setShowPassword(!showPassword)}
                      color="primary"
                    />
                  }
                  label={t("signIn.showPassword.label")}
                  sx={{ paddingTop: "10px" }}
                />
              </Box>
              <Box sx={{ width: "100%" }}>
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  sx={{ fontSize: "18px" }}
                  fullWidth
                  disabled={isSubmitting || !token}
                >
                  {t("signIn.login.label")}
                </Button>
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "center",
                    paddingTop: "15px",
                    flexWrap: "wrap",
                  }}
                >
                  {t("signIn.account.label")}
                  <NavLink
                    sx={{
                      color: color.textSecondary,
                      fontWeight: "bold",
                      paddingLeft: "10px",
                    }}
                    to={RoutePath.SIGN_UP}
                  >
                    {t("signIn.signUp.label")}
                  </NavLink>
                </Box>
              </Box>
            </Box>
            <GoogleReCaptcha
              onVerify={handleVerifyReCaptcha}
              refreshReCaptcha={refreshReCaptcha}
            />
          </Form>
        )}
      </Formik>
    </>
  );
};
