import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { StartPage } from "../pages/StartPage";
import { RoutePath } from "./RoutePath ";
import { ArticlePage } from "../pages/ArticlePage";
import { ScrollToTop } from "./Scroll";
import { AllArticlesPage } from "../pages/AllArticlesPage";
import { AllArticlesInTagPage } from "../pages/AllArticlesInTagPage";
import { AllTagsPage } from "../pages/AllTagsPage";
import { SignInPage } from "../pages/SignIn/SignInPage";
import { SignUpPage } from "../pages/SignUp/SignUpPage";
import { AddArticle } from "../pages/AddArticle/AddArticle";
import { ProtectedRoute } from "./ProtectedRoute";
import { EditTags } from "../pages/EditTags/EditTags";
import { EditArticleStatus } from "../pages/EditArticleStatus/EditArticleStatus";
import { PageNotFound } from "../pages/PageNotFound";

export const Routing = () => {
  return (
    <Router>
      <ScrollToTop />
      <Routes>
        <Route path={RoutePath.START} Component={StartPage} />
        <Route path={RoutePath.ARTICLE} Component={ArticlePage} />
        <Route path={RoutePath.ALL_ARTICLES} Component={AllArticlesPage} />
        <Route
          path={RoutePath.ALL_TAGS_ARTICLES}
          Component={AllArticlesInTagPage}
        />
        <Route path={RoutePath.ALL_TAGS} Component={AllTagsPage} />
        <Route path={RoutePath.SIGN_IN} Component={SignInPage} />
        <Route path={RoutePath.SIGN_UP} Component={SignUpPage} />
        <Route path={RoutePath.PAGE_NOT_FOUND} Component={PageNotFound} />
        <Route
          path={RoutePath.ADD_ARTICLE}
          element={
            <ProtectedRoute>
              <AddArticle />
            </ProtectedRoute>
          }
        />
        <Route
          path={RoutePath.EDIT_TAGS}
          element={
            <ProtectedRoute isAdminAllowed={true}>
              <EditTags />
            </ProtectedRoute>
          }
        />
        <Route
          path={RoutePath.EDIT_ARTICLES_STATUS}
          element={
            <ProtectedRoute isAdminAllowed={true}>
              <EditArticleStatus />
            </ProtectedRoute>
          }
        />
      </Routes>
    </Router>
  );
};
