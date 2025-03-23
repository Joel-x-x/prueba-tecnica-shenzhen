export interface ILogin {
  email: string;
  password: string;
}

export interface ILoginResponse {
  result: {
    accessToken: string;
    refreshToken: string;
  }
  success: boolean;
}
