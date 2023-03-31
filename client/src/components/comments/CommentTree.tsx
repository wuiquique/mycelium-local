import {
  Button,
  Card,
  CardActions,
  CardContent,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import { useCallback, useState } from "react";
import { MdArrowDownward, MdArrowUpward } from "react-icons/md";

type Comment = {
  id: number;
  message: string;
  created: number;
  updated: number;
  productId: number;
  userName: string;
  votes: number | undefined;
  children: any[];
};

export default function CommentTree({
  comments,
  productId,
  onPostComment,
}: {
  comments: Comment[];
  productId: number;
  onPostComment: () => void;
}) {
  const [newComment, setNewComment] = useState<string[]>([]);

  const postComment = useCallback(
    (parentCommentId: number, index: number) => {
      axios
        .post(`/api/comment/${productId}`, {
          commentId: parentCommentId,
          message: newComment[index],
        })
        .then(() => {
          onPostComment();
          const temp = [...newComment];
          temp[index] = "";
          setNewComment(temp);
        });
    },
    [newComment, onPostComment, productId]
  );

  return (
    <>
      {comments.map((c, i) => (
        <div key={i} className="my-5 gap-2 flex-col flex">
          <Card>
            <CardContent>
              <Typography variant="h5" component="div">
                {c.userName}
              </Typography>
              <p>{c.message}</p>
            </CardContent>
            <CardActions>
              <Button size="small">
                <MdArrowUpward />
              </Button>
              <span>{c.votes}</span>
              <Button size="small">
                <MdArrowDownward />
              </Button>
            </CardActions>
          </Card>
          <Card>
            <CardContent>
              <TextField
                className="block w-full min-h-[4rem]"
                multiline
                InputProps={{
                  className: "block w-full min-h-[4rem]",
                }}
                variant="standard"
                placeholder="Comment"
                value={newComment[i]}
                onChange={(e) => {
                  const temp = [...newComment];
                  temp[i] = e.currentTarget.value;
                  setNewComment(temp);
                }}
              />
              <Button className="block" onClick={() => postComment(c.id, i)}>
                Post
              </Button>
            </CardContent>
          </Card>
          <div className="pl-5">
            <CommentTree
              comments={c.children}
              productId={productId}
              onPostComment={onPostComment}
            />
          </div>
        </div>
      ))}
    </>
  );
}
