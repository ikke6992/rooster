import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = 'http://localhost:8080/api/v1/teachers';

  constructor(private http: HttpClient) {}

  getTeachers(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  putAvailability(id: number, data: any): Observable<any> {
    return this.http.put<any>(this.apiUrl + '/edit/' + id, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }
}
