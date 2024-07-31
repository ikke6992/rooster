import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private getUrl = 'http://localhost:8080/api/v1/groups/';
  private postUrl = 'http://localhost:8080/api/v1/groups/new';

  constructor(private http: HttpClient) {}

  getData(): Observable<any> {
    return this.http.get<any>(this.getUrl);
  }

  postData(data: any): Observable<any> {
    return this.http.post<any>(this.postUrl, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }
}
