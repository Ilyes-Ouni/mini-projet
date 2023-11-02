export class Users {
    id?: number;
    firstname?: string;
    lastname?: string;
    email?: string;
    password?: string;
    role?: string;
    imagePath?: string;
    tokens?: any[] = [];
    enabled?: boolean;
    accountNonExpired?: boolean;
    credentialsNonExpired?: boolean;
    username?: string;
    authorities?: Authority[] = [];
    accountNonLocked?: boolean;
  }
  
  
  export interface Authority {
    authority?: string;
  }
  