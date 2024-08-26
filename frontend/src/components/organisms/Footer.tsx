import { AppBar, Box, Toolbar } from "@mui/material";
import { useTranslation } from "react-i18next";
import logo from "../../assets/sensinews-logo.png";
import { MainTagsId, TextVariant } from "../../constants/Const";
import { color } from "../../styles/colors";
import { Heading } from "../atoms/Heading";
import { NavLink } from "../atoms/Link";
import { RoutePath } from "../../router/RoutePath ";

export const Footer = () => {
  const { t } = useTranslation();
  return (
    <AppBar
      sx={{ backgroundColor: color.white, padding: "10px" }}
      position="relative"
      elevation={0}
    >
      <Toolbar
        sx={{
          display: "flex",
          flexDirection: { xs: "column", sm: "row" },
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: { xs: "column", sm: "row" },
            justifyContent: "space-around",
            width: "100%",
            paddingTop: "20px",
            alignItems: "center",
          }}
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              paddingBottom: "20px",
            }}
          >
            <img src={logo} alt="logo" />
          </Box>
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              paddingBottom: { xs: "20px", sm: "0px" },
            }}
          >
            <Heading
              variant={TextVariant.H6}
              sx={{ fontWeight: "bold", paddingBottom: "10px" }}
            >
              Sensi News
            </Heading>
            <Box
              sx={{
                paddingTop: "5px",
                display: "flex",
                flexDirection: "column",
              }}
            >
              <NavLink
                sx={{ paddingBottom: "5px" }}
                to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.TECHNOLOGY}
              >
                {t("navbarFooter.technology.label")}
              </NavLink>
              <NavLink
                sx={{ paddingBottom: "5px" }}
                to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.GADGET}
              >
                {t("navbarFooter.gadget.label")}
              </NavLink>
              <NavLink
                sx={{ paddingBottom: "5px" }}
                to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.SOFTWARE}
              >
                {t("navbarFooter.software.label")}
              </NavLink>
              <NavLink
                sx={{ paddingBottom: "5px" }}
                to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.APPS}
              >
                {t("navbarFooter.apps.label")}
              </NavLink>
              <NavLink sx={{ paddingBottom: "5px" }} to={RoutePath.ALL_TAGS}>
                {t("navbarFooter.other.label")}
              </NavLink>
            </Box>
          </Box>
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              paddingBottom: { xs: "20px", sm: "0px" },
            }}
          ></Box>
        </Box>
      </Toolbar>
    </AppBar>
  );
};
