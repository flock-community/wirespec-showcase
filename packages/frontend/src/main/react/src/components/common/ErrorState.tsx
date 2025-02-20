import { ShowcaseError } from "../../utils.ts";

export type ErrorProps = {
  error: ShowcaseError;
};

export const ErrorState = ({ error }: ErrorProps) => (
  <div>
    <p>{error.message}</p>
    <pre>{JSON.stringify(error.cause, null, 4)}</pre>
  </div>
);
