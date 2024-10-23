import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlFields = 'http://localhost:8080/api/v1/fields';

  constructor(private http: HttpClient) {}

  getFields(): Observable<any> {
    return this.http.get<any>(this.apiUrlFields);
  }

  postField(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrlFields + '/new', data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }

  putField(fieldId: number, data: any): Observable<any> {
    return this.http.put<any>(
      this.apiUrlFields + '/' + fieldId + '/edit',
      data,
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }
}
