import {
  Button,
  Card,
  CardActions,
  CardContent,
  Typography,
} from "@mui/material";
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

export default function CommentTree({ comments }: { comments: Comment[] }) {
  return (
    <>
      {comments.map((c, i) => (
        <div key={i} className="my-5">
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
          <div className="pl-5">
            <CommentTree comments={c.children} />
          </div>
        </div>
      ))}
    </>
  );
}
