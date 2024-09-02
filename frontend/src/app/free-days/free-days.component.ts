import { Component } from '@angular/core';
import { DataService } from './data-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-free-days',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './free-days.component.html',
  styleUrl: './free-days.component.css'
})
export class FreeDaysComponent {
  freeDays: FreeDays[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getData().subscribe((response: any[]) => {
      this.freeDays = response;
      console.log(response);
    });
  }

  remove(id: number){
    this.dataService.removeDay(id).subscribe();
    (this.freeDays.find((freeDay) => freeDay.id === id) || this.freeDays[id]).isDeleted = true;
  }
}

interface FreeDays {
  id: number;
  name: string;
  date: Date;
  isDeleted: boolean
}