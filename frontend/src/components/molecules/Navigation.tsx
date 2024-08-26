import {
  Box,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import { useTranslation } from "react-i18next";
import { useContext, useState } from "react";
import { LinkDirection, MainTagsId, UserRoles } from "../../constants/Const";
import { NavLink } from "../atoms/Link";
import { LanguageSelect } from "../atoms/LanguageSelect";
import { RoutePath } from "../../router/RoutePath ";
import { AuthTokenStorage } from "../../storage/localStorage";
import { AuthContext } from "../../providers/AuthProvider";
import { useLocation } from "react-router-dom";

type NavigationProps = {
  direction?: LinkDirection;
  styleForLink?: object;
};

export const Navigation = ({
  direction = LinkDirection.ROW,
  styleForLink,
}: NavigationProps) => {
  const { t } = useTranslation();
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const authTokenStorage = new AuthTokenStorage();
  const { loggedUser, setLoggedUser } = useContext(AuthContext);
  const pathname = useLocation().pathname;
  const activeTag = pathname.replace("/articles/", "");

  const handleDrawerOpen = () => {
    setIsDrawerOpen(true);
  };

  const handleDrawerClose = () => {
    setIsDrawerOpen(false);
  };
  const handleSignOut = () => {
    authTokenStorage.removeToken();
    setLoggedUser(undefined);
  };

  const navLinks = (
    <>
      <NavLink
        sx={{
          ...styleForLink,
          fontWeight: activeTag === MainTagsId.TECHNOLOGY ? "bold" : "normal",
        }}
        to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.TECHNOLOGY}
      >
        {t("navbarFooter.technology.label")}
      </NavLink>
      <NavLink
        sx={{
          ...styleForLink,
          fontWeight: activeTag === MainTagsId.GADGET ? "bold" : "normal",
        }}
        to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.GADGET}
      >
        {t("navbarFooter.gadget.label")}
      </NavLink>
      <NavLink
        sx={{
          ...styleForLink,
          fontWeight: activeTag === MainTagsId.SOFTWARE ? "bold" : "normal",
        }}
        to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.SOFTWARE}
      >
        {t("navbarFooter.software.label")}
      </NavLink>
      <NavLink
        sx={{
          ...styleForLink,
          fontWeight: activeTag === MainTagsId.APPS ? "bold" : "normal",
        }}
        to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.APPS}
      >
        {t("navbarFooter.apps.label")}
      </NavLink>
      <NavLink
        sx={{
          ...styleForLink,
          fontWeight: pathname === RoutePath.ALL_TAGS ? "bold" : "normal",
        }}
        to={RoutePath.ALL_TAGS}
      >
        {t("navbarFooter.other.label")}
      </NavLink>
    </>
  );

  return (
    <Box
      sx={{
        paddingTop: "5px",
        display: "flex",
        flexDirection: direction,
        alignItems: "center",
      }}
    >
      <Box sx={{ display: { xs: "flex", xl: "none" } }}>
        <IconButton
          edge="start"
          color="default"
          aria-label="menu"
          onClick={handleDrawerOpen}
        >
          <MenuIcon />
        </IconButton>
      </Box>

      <Box
        sx={{
          display: { xs: "none", xl: "flex" },
          flexDirection: direction,
        }}
      >
        {navLinks}
      </Box>

      <Drawer
        anchor="top"
        open={isDrawerOpen}
        onClose={handleDrawerClose}
        variant="persistent"
      >
        <Box>
          <IconButton onClick={handleDrawerClose}>
            <CloseIcon />
          </IconButton>
          <LanguageSelect displayMd="none" displayXs="flex" />
          <List>
            <NavLink to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.TECHNOLOGY}>
              <ListItem onClick={handleDrawerClose}>
                <ListItemText primary={t("navbarFooter.technology.label")} />
              </ListItem>
            </NavLink>
            <NavLink to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.GADGET}>
              <ListItem onClick={handleDrawerClose}>
                <ListItemText primary={t("navbarFooter.gadget.label")} />
              </ListItem>
            </NavLink>
            <NavLink to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.SOFTWARE}>
              <ListItem onClick={handleDrawerClose}>
                <ListItemText primary={t("navbarFooter.software.label")} />
              </ListItem>
            </NavLink>
            <NavLink to={RoutePath.ALL_ARTICLES + "/" + MainTagsId.APPS}>
              <ListItem onClick={handleDrawerClose}>
                <ListItemText primary={t("navbarFooter.apps.label")} />
              </ListItem>
            </NavLink>
            <NavLink to={RoutePath.ALL_TAGS}>
              <ListItem onClick={handleDrawerClose}>
                <ListItemText primary={t("navbarFooter.other.label")} />
              </ListItem>
            </NavLink>
            {loggedUser?.role === UserRoles.ADMIN && (
              <NavLink to={RoutePath.EDIT_TAGS}>
                <ListItem onClick={handleDrawerClose}>
                  <ListItemText primary={t("navbar.editTags.label")} />
                </ListItem>
              </NavLink>
            )}
            {!authTokenStorage.isTokenPresent() ? (
              <NavLink to={RoutePath.SIGN_IN}>
                <ListItem onClick={handleDrawerClose}>
                  <ListItemText primary={t("navbar.signIn.label")} />
                </ListItem>
              </NavLink>
            ) : (
              <Box>
                <NavLink to={RoutePath.ADD_ARTICLE}>
                  <ListItem onClick={handleDrawerClose}>
                    <ListItemText primary={t("navbar.addArticle.label")} />
                  </ListItem>
                </NavLink>
                <ListItem onClick={handleDrawerClose}>
                  <Box onClick={handleSignOut}>
                    <NavLink to={RoutePath.START}>
                      {t("navbar.signOut.label")}
                    </NavLink>
                  </Box>
                </ListItem>
              </Box>
            )}
          </List>
        </Box>
      </Drawer>
    </Box>
  );
};
