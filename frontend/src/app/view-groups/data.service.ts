import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlFields = 'http://localhost:8080/api/v1/fields';
  private apiUrlGroups = 'http://localhost:8080/api/v1/groups';

  constructor(private http: HttpClient) {}

  getFields(): Observable<any> {
    return this.http.get<any>(this.apiUrlFields);
  }

  getGroups(): Observable<any> {
    return this.http.get<any>(this.apiUrlGroups);
  }

  postGroup(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrlGroups + '/new', data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  rescheduleGroup(groupNumber: number): Observable<any> {
    return this.http.put<any>(
      this.apiUrlGroups + '/' + groupNumber + '/reschedule',
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }

  addVacation(groupNumber: number, data: any): Observable<any> {
    return this.http.put<any>(
      this.apiUrlGroups + '/' + groupNumber + '/addVacation',
      data,
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }
}
