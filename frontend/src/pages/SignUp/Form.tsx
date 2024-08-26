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
import {
  initialValues,
  getValidationSchema,
  SignUpFormValues,
} from "./validationSchema";
import { useNavigate } from "react-router-dom";
import { RoutePath } from "../../router/RoutePath ";
import { getUser, signUp } from "../../requests/auth";
import { AuthContext } from "../../providers/AuthProvider";
import { GoogleReCaptcha } from "react-google-recaptcha-v3";
import { NavLink } from "../../components/atoms/Link";
import { color } from "../../styles/colors";

export const SignUpForm = () => {
  const { t } = useTranslation();
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const { setLoggedUser } = useContext(AuthContext);
  const [token, setToken] = useState("");
  const [refreshReCaptcha, setRefreshReCaptcha] = useState(false);

  const handleSignUp = async (
    values: SignUpFormValues,
    { setSubmitting }: FormikHelpers<SignUpFormValues>,
  ) => {
    if (!token) return;
    await signUp(values, token);
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
        onSubmit={handleSignUp}
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
                minWidth: "auto",
              }}
            >
              <Box sx={{ width: "100%", padding: "10px" }}>
                <Field
                  as={TextField}
                  name="username"
                  type="text"
                  label={t("signIn.username.label")}
                  variant="standard"
                  fullWidth
                  error={touched.username && !!errors.username}
                  helperText={
                    touched.username && errors.username
                      ? touched.username && errors.username
                      : t("signIn.usernameHelper.label")
                  }
                  InputProps={{
                    style: { fontSize: "18px" },
                  }}
                  InputLabelProps={{
                    style: { fontSize: "18px" },
                  }}
                  sx={{ paddingBottom: "10px" }}
                />
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
              </Box>
              <Box sx={{ width: "100%", padding: "10px" }}>
                <Field
                  as={TextField}
                  name="confirmPassword"
                  type={showPassword ? "text" : "password"}
                  label={t("signIn.confirmPassword.label")}
                  variant="standard"
                  fullWidth
                  error={touched.confirmPassword && !!errors.confirmPassword}
                  helperText={touched.confirmPassword && errors.confirmPassword}
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
                  {t("signIn.signUp.label")}
                </Button>
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "center",
                    paddingTop: "15px",
                    flexWrap: "wrap",
                  }}
                >
                  {t("signIn.haveAccount.label")}
                  <NavLink
                    sx={{
                      color: color.textSecondary,
                      fontWeight: "bold",
                      paddingLeft: "10px",
                    }}
                    to={RoutePath.SIGN_IN}
                  >
                    {t("signIn.signIn.label")}
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
