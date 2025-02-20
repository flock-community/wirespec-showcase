export class ShowcaseError extends Error {
  readonly cause: object;
  constructor(message: string, cause?: any) {
    super(message);
    this.name = "ShowcaseError";
    this.cause = cause;
  }
}
