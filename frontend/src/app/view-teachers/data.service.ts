import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private getTeachersUrl = 'http://localhost:8080/api/v1/teachers/';
  private putAvailabilityUrl = 'http://localhost:8080/api/v1/teachers/change';

  constructor(private http: HttpClient) {}

  getTeachers(): Observable<any> {
    return this.http.get<any>(this.getTeachersUrl);
  }

  putAvailability(data: any): Observable<any> {
    return this.http.put<any>(this.putAvailabilityUrl, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }
}
