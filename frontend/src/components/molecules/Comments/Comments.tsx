import React from "react";
import DoneIcon from "@mui/icons-material/Done";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import {
  Box,
  Card,
  CardContent,
  IconButton,
  Menu,
  MenuItem,
  TextField,
} from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { TextVariant, UserRoles } from "../../../constants/Const";
import { AuthContext } from "../../../providers/AuthProvider";
import {
  Comment,
  commentReply,
  deleteComment,
  getArticleComments,
  updateComment,
} from "../../../requests/articleList";
import { color } from "../../../styles/colors";
import { Heading } from "../../atoms/Heading";
import QuestionAnswerIcon from "@mui/icons-material/QuestionAnswer";
import { CommentForm } from "./Form";
import { CommentHeader } from "./CommentHeader";

type CommentsProps = {
  articleId: string;
};

export const Comments = ({ articleId }: CommentsProps) => {
  const [comments, setComments] = useState<Comment[]>([]);
  const { t } = useTranslation();
  const { loggedUser } = useContext(AuthContext);
  const [anchorEl, setAnchorEl] = useState<{
    [key: string]: HTMLElement | null;
  }>({});
  const [editingCommentId, setEditingCommentId] = useState<string | null>(null);
  const [editingContent, setEditingContent] = useState<string>("");
  const [replyCommentId, setReplyCommentId] = useState<string | null>(null);
  const [replyContent, setReplyContent] = useState<string>("");

  const fetchComments = async () => {
    const comments = await getArticleComments(articleId);
    const sortedComments = comments.sort((a, b) => {
      if (a.userAccountUsername === loggedUser?.username) return -1;
      if (b.userAccountUsername === loggedUser?.username) return 1;
      return 0;
    });
    setComments(sortedComments);
  };

  useEffect(() => {
    fetchComments();
  }, [articleId]);

  const handleMenuOpen = (
    event: React.MouseEvent<HTMLElement>,
    commentId: string,
  ) => {
    setAnchorEl((prevState) => ({
      ...prevState,
      [commentId]: event.currentTarget,
    }));
  };

  const handleMenuClose = (commentId: string) => {
    setAnchorEl((prevState) => ({ ...prevState, [commentId]: null }));
  };

  const handleDeleteComment = async (commentId: string) => {
    await deleteComment(commentId);
    fetchComments();
  };

  const handleEditComment = (commentId: string, content: string) => {
    setEditingCommentId(commentId);
    setEditingContent(content);
    handleMenuClose(commentId);
  };

  const handleUpdateComment = async (commentId: string) => {
    if (editingCommentId) {
      await updateComment(articleId, commentId, editingContent);
      setEditingCommentId(null);
      setEditingContent("");
      fetchComments();
    }
  };

  const handleReplyCommentInput = (commentId: string) => {
    setReplyCommentId(commentId);
  };

  const handleReplyComment = async (commentId: string) => {
    if (replyCommentId) {
      await commentReply(replyContent, articleId, commentId);
      setReplyCommentId(null);
      setReplyContent("");
      fetchComments();
    }
  };

  return (
    <Box>
      <Heading variant={TextVariant.H4} sx={{ marginBottom: "20px" }}>
        {t("articleContent.comments.label")}
      </Heading>

      <CommentForm articleId={articleId} onCommentSubmitted={fetchComments} />
      {comments.map((comment) => (
        <Card key={comment.id} sx={{ marginBottom: "10px" }}>
          <CardContent
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <Box sx={{ width: "100%" }}>
              <CommentHeader
                author={comment.userAccountUsername}
                date={comment.createdAt}
                version={comment.version}
              />

              {editingCommentId === comment.id ? (
                <Box sx={{ display: "flex", alignItems: "center" }}>
                  <TextField
                    value={editingContent}
                    onChange={(e) => setEditingContent(e.target.value)}
                    fullWidth
                    multiline
                    required
                    error={editingContent.trim() === ""}
                    helperText={
                      editingContent.trim() === ""
                        ? t("articleContent.commentRequired.label")
                        : ""
                    }
                  />
                  <IconButton
                    type="submit"
                    onClick={() => handleUpdateComment(comment.id)}
                    sx={{ marginLeft: "20px" }}
                    disabled={editingContent.trim() === ""}
                  >
                    <DoneIcon />
                  </IconButton>
                </Box>
              ) : (
                <Box>
                  <Heading variant={TextVariant.H6} sx={{ padding: "0px" }}>
                    {comment.content}
                  </Heading>
                  {comment.replies.length !== 0 && (
                    <Box sx={{ borderTop: "1px solid gray" }} />
                  )}
                  {comment.replies.map((reply) => (
                    <Box
                      key={reply.id}
                      sx={{
                        width: "100%",
                        paddingLeft: "20px",
                      }}
                    >
                      <Heading variant={TextVariant.SUBTITLE1}>
                        {reply.userAccountUsername}: {reply.content}
                      </Heading>
                    </Box>
                  ))}

                  {replyCommentId === comment.id && (
                    <Box sx={{ display: "flex", alignItems: "center" }}>
                      <TextField
                        value={replyContent}
                        onChange={(e) => setReplyContent(e.target.value)}
                        fullWidth
                        multiline
                        required
                        error={replyContent.trim() === ""}
                      />
                      <IconButton
                        type="submit"
                        onClick={() => handleReplyComment(comment.id)}
                        sx={{ marginLeft: "20px" }}
                        disabled={replyContent.trim() === ""}
                      >
                        <DoneIcon />
                      </IconButton>
                    </Box>
                  )}
                </Box>
              )}
            </Box>
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "end",
              }}
            >
              {(loggedUser?.role === UserRoles.ADMIN ||
                loggedUser?.role === UserRoles.USER) && (
                <IconButton onClick={() => handleReplyCommentInput(comment.id)}>
                  <QuestionAnswerIcon />
                </IconButton>
              )}
              {(loggedUser?.username === comment.userAccountUsername ||
                loggedUser?.role === UserRoles.ADMIN) && (
                <>
                  <IconButton onClick={(e) => handleMenuOpen(e, comment.id)}>
                    <MoreVertIcon />
                  </IconButton>
                  <Menu
                    anchorEl={anchorEl[comment.id]}
                    open={Boolean(anchorEl[comment.id])}
                    onClose={() => handleMenuClose(comment.id)}
                    sx={{
                      "& .MuiPaper-root": {
                        boxShadow: color.boxShadow,
                      },
                    }}
                  >
                    <MenuItem
                      onClick={() =>
                        handleEditComment(comment.id, comment.content)
                      }
                    >
                      {t("articleContent.edit.label")}
                    </MenuItem>
                    <MenuItem
                      onClick={() => handleDeleteComment(comment.id)}
                      sx={{ color: color.deleted }}
                    >
                      {t("articleContent.delete.label")}
                    </MenuItem>
                  </Menu>
                </>
              )}
            </Box>
          </CardContent>
        </Card>
      ))}
    </Box>
  );
};
