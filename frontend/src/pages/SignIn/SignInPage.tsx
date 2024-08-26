import { Box } from "@mui/material";
import logo from "../../assets/sensinews-logo.png";
import { LanguageSelect } from "../../components/atoms/LanguageSelect";
import { NavLink } from "../../components/atoms/Link";
import { SignInForm } from "./Form";

export const SignInPage = () => {
  return (
    <Box sx={{ margin: "10px" }}>
      <LanguageSelect displayMd="flex" displayXs="flex" />
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          paddingTop: "20px",
        }}
      >
        <NavLink to="/">
          <img
            src={logo}
            alt="logo"
            style={{ width: "300px", maxWidth: "100%", height: "auto" }}
          />
        </NavLink>
        <SignInForm />
      </Box>
    </Box>
  );
};
