export interface IPost {
  id?: string;
  titulo: string;
  contenido: string;
  isPublic: boolean;
  created?: Date;
  userId?: string;
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

