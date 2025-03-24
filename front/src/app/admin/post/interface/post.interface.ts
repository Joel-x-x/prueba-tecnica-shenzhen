export interface IPost {
  id?: string;
  title: string;
  content: string;
  isPublic: boolean;
  created?: Date;
  userId?: string;
  author?: string;
}

export interface IPostResponse{
  code: number;
  error: string[];
  success: boolean;
  result: IPost;
}

export interface IPostPaginateResponse{
  result: {
      content: IPost[];
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

