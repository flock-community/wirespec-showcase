import { serialization, wirespecWebClient } from "./WirespecWebClient.ts";
import {
  GetMoveMoneyTransactionById,
  GetMoveMoneyTransactions,
  Login,
  Logout,
  SendMoney,
} from "./wirespec/Move-money-api.ts";

class MovemoneyClient
  implements
    GetMoveMoneyTransactions.Handler,
    GetMoveMoneyTransactionById.Handler,
    SendMoney.Handler,
    Login.Handler,
    Logout.Handler
{
  private readonly getMoveMoneyTransactionsClient =
    GetMoveMoneyTransactions.client(serialization);
  private readonly getMoveMoneyTransactionByIdClient =
    GetMoveMoneyTransactionById.client(serialization);
  private readonly sendMoneyClient = SendMoney.client(serialization);
  private readonly loginClient = Login.client(serialization);
  private readonly logoutClient = Logout.client(serialization);

  sendMoney: (request: SendMoney.Request) => Promise<SendMoney.Response> =
    async (request) => {
      let rawRequest = this.sendMoneyClient.to(request);
      let rawResponse = await wirespecWebClient(rawRequest);
      return this.sendMoneyClient.from(rawResponse);
    };

  getMoveMoneyTransactionById: (
    request: GetMoveMoneyTransactionById.Request,
  ) => Promise<GetMoveMoneyTransactionById.Response> = async (request) => {
    let rawRequest = this.getMoveMoneyTransactionByIdClient.to(request);
    let rawResponse = await wirespecWebClient(rawRequest);
    return this.getMoveMoneyTransactionByIdClient.from(rawResponse);
  };

  getMoveMoneyTransactions: (
    request: GetMoveMoneyTransactions.Request,
  ) => Promise<GetMoveMoneyTransactions.Response> = async (request) => {
    let rawRequest = this.getMoveMoneyTransactionsClient.to(request);
    let rawResponse = await wirespecWebClient(rawRequest);
    return this.getMoveMoneyTransactionsClient.from(rawResponse);
  };

  login: (request: Login.Request) => Promise<Login.Response> = async (
    request,
  ) => {
    let rawRequest = this.loginClient.to(request);
    let rawResponse = await wirespecWebClient(rawRequest);
    return this.loginClient.from(rawResponse);
  };

  logout: (request: Logout.Request) => Promise<Logout.Response> = async (
    request,
  ) => {
    let rawRequest = this.logoutClient.to(request);
    let rawResponse = await wirespecWebClient(rawRequest);
    return this.logoutClient.from(rawResponse);
  };
}

export const moveMoneyClient = new MovemoneyClient();
