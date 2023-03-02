import {
  Button,
  Card,
  CardActions,
  CardContent,
  Typography,
} from "@mui/material";
import React, { Fragment } from "react";
import { MdArrowDownward, MdArrowUpward } from "react-icons/md";

type Comment = {
  author: string;
  content: string;
  votes: number;
  replies: Comment[];
};

export default function CommentTree({ comments }: { comments: Comment[] }) {
  return (
    <>
      {comments.map((c, i) => (
        <div key={i} className="my-5">
          <Card>
            <CardContent>
              <Typography variant="h5" component="div">
                {c.author}
              </Typography>
              <p>{c.content}</p>
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
          <div className="pl-5">
            <CommentTree comments={c.replies} />
          </div>
        </div>
      ))}
    </>
  );
}
