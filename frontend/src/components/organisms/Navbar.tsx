import { Box } from "@mui/material";
import AppBar from "@mui/material/AppBar";
import { useTheme } from "@mui/material/styles";
import Toolbar from "@mui/material/Toolbar";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useContext } from "react";
import { useTranslation } from "react-i18next";
import logo from "../../assets/sensinews-logo.png";
import { AuthContext } from "../../providers/AuthProvider";
import { RoutePath } from "../../router/RoutePath ";
import { AuthTokenStorage } from "../../storage/localStorage";
import { color } from "../../styles/colors";
import { LanguageSelect } from "../atoms/LanguageSelect";
import { NavLink } from "../atoms/Link";
import { Navigation } from "../molecules/Navigation";
import { UserRoles } from "../../constants/Const";

export const Navbar = () => {
  const { t } = useTranslation();
  const theme = useTheme();
  const authTokenStorage = new AuthTokenStorage();
  const { loggedUser, setLoggedUser } = useContext(AuthContext);

  const handleSignOut = () => {
    authTokenStorage.removeToken();
    setLoggedUser(undefined);
  };

  return (
    <AppBar
      sx={{
        backgroundColor: color.white,
        paddingX: "10px",
        fontSize: "14px",
      }}
      position="sticky"
      elevation={0}
    >
      <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <NavLink to="/">
            <img
              style={{
                paddingRight: "40px",
                maxHeight: "60px",
              }}
              src={logo}
              alt="logo"
            ></img>
          </NavLink>
          <Box sx={{ display: { xs: "none", xl: "flex" }, marginLeft: "auto" }}>
            <Navigation />
          </Box>
        </Box>
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <Box sx={{ display: { xs: "flex", xl: "none" } }}>
            <Navigation />
          </Box>
          {loggedUser?.role === UserRoles.ADMIN && (
            <>
              <Box
                sx={{
                  display: { xs: "none", xl: "flex" },
                  alignItems: "center",
                }}
              >
                <NavLink to={RoutePath.EDIT_TAGS}>
                  {t("navbar.editTags.label")}
                </NavLink>
              </Box>
              <Box
                sx={{
                  display: { xs: "none", xl: "flex" },
                  alignItems: "center",
                }}
              >
                <NavLink to={RoutePath.EDIT_ARTICLES_STATUS}>
                  {t("navbar.editArticlesStatus.label")}
                </NavLink>
              </Box>
            </>
          )}
          {!authTokenStorage.isTokenPresent() ? (
            <Box
              sx={{ display: { xs: "none", xl: "flex" }, alignItems: "center" }}
            >
              <NavLink to={RoutePath.SIGN_IN}>
                {t("navbar.signIn.label")}
              </NavLink>
              <LanguageSelect displayMd="flex" displayXs="none" />
            </Box>
          ) : (
            <Box
              sx={{ display: { xs: "none", xl: "flex" }, alignItems: "center" }}
            >
              <NavLink to={RoutePath.ADD_ARTICLE}>
                {t("navbar.addArticle.label")}
              </NavLink>
              <Box onClick={handleSignOut}>
                <NavLink to={RoutePath.START}>
                  {t("navbar.signOut.label")}
                </NavLink>
              </Box>
              <LanguageSelect displayMd="flex" displayXs="none" />
            </Box>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};
