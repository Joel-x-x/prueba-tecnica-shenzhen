export interface ILogin {
  email: string;
  password: string;
}

export interface ILoginResponse {
  acessToken: string;
  refreshToken: string;
}
