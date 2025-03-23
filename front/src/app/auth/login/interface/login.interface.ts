export interface ILogin {
  email: string;
  password: string;
}

export interface ILoginResponse {
  result: {
    acessToken: string;
    refreshToken: string;
  }
  success: boolean;
}
