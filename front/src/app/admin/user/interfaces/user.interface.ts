export interface IUser{
  id: string;
  email: string;
  firstNames: string;
  lastNames: string;
  roles: IRole[];
}

export interface IUserResponse{
  code: number;
  error: string[];
  success: boolean;
  result: IUser;
}

export interface IUserRequest{
  id: string;
  email: string;
  firstNames: string;
  lastNames: string;
  password?: string;
  role: string;
}

export interface IUserPaginateResponse{
  result: {
      content: IUser[];
      pageable: {
          pageNumber: number;
          pageSize: number;
          sort: {
              empty: boolean;
              sorted: boolean;
              unsorted: boolean;
          };
          offset: number;
          paged: boolean;
          unpaged: boolean;
      };
      last: boolean;
      totalPages: number;
      totalElements: number;
      first: boolean;
      size: number;
      number: number;
      sort: {
          empty: boolean;
          sorted: boolean;
          unsorted: boolean;
      };
      numberOfElements: number;
      empty: boolean;
  };
  errors: null;
  code: number;
  success: boolean;

}
export interface IRole{
  id: string;
  name: string;
}

