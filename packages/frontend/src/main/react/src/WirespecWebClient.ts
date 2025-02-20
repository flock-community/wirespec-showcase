import { Wirespec } from "./wirespec/Audit-api.ts";

export const serialization: Wirespec.Serialization = {
  deserialize<T>(raw: string | undefined): T {
    if (raw === undefined) {
      throw new Error("Wirespec could not deserialize undefined");
    } else if (typeof raw === "object") {
      return raw as T;
    } else {
      return JSON.parse(raw) as T;
    }
  },
  serialize<T>(type: T): string {
    if (typeof type === "string") {
      return type;
    } else {
      return JSON.stringify(type);
    }
  },
};

const toQueryString = (queries: Record<string, string>) => {
  if (Object.keys(queries).length === 0) {
    return "";
  }

  return (
    "?" +
    Object.entries(queries)
      .map(
        ([key, value]) =>
          `${encodeURIComponent(key)}=${encodeURIComponent(value)}`,
      )
      .join("&")
  );
};

const baseUrl = "/";
export const wirespecWebClient: (
  request: Wirespec.RawRequest,
) => Promise<Wirespec.RawResponse> = async (request) => {
  const response = await fetch(
    baseUrl + request.path.join("/") + toQueryString(request.queries),
    {
      method: request.method,
      headers: request.headers,
      body: request.body,
    },
  );

  return {
    status: response.status,
    body: await body(response),
    headers: Object.fromEntries(response.headers.entries()) as Record<
      string,
      string
    >,
  };
};

/**
 * Parse the body of a fetch response.
 */
const body = async (response: Response) => {
  try {
    const text = await response.text(); // Parse it as text
    return JSON.parse(text);
  } catch (err) {
    return null;
  }
};
