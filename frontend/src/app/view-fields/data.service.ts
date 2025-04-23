import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlFields = `${environment.apiUrl}/api/v1/fields`;

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
