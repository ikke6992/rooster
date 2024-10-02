import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DataService } from './data.service';
import { ScheduledDayComponent } from '../scheduled-day/scheduled-day.component';
import { ModalComponent } from '../modal/modal.component';
import { OverrideComponent } from './override/override.component';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [
    CommonModule,
    ScheduledDayComponent,
    ModalComponent,
    OverrideComponent,
  ],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css',
})
export class ScheduleComponent {
  data: Scheduledday[] = [];
  freeDays: FreeDay[] = [];

  month = new Date().getMonth() + 1;
  year = new Date().getFullYear();

  days: Day[] = this.daysInMonth(this.year, this.month);

  selectedModal: any = null;
  destinationdate: string = '';
  destinationclassroom: number = 0;

  errorMsg!: string;

  TOTAL_CLASSROOMS: number[] = Array(6)
    .fill(0)
    .map((_, index) => index + 1);

  daysInMonth(year: number, month: number): Day[] {
    const array = Array(new Date(year, month, 0).getDate())
      .fill(0)
      .map((_, index) => index + 1);
    const days: Day[] = [];
    array.forEach(
      (value, index) =>
        (days[index] = {
          id: value,
          isWeekend: this.checkIfWeekend(year, month, value),
          isFreeDay: this.checkIfFreeDay(year, month, value),
        })
    );
    return days;
  }

  checkIfWeekend(year: number, month: number, day: number): boolean {
    const dayOfWeek = new Date(year, month - 1, day);
    return dayOfWeek.getDay() === 0 || dayOfWeek.getDay() === 6;
  }

  checkIfFreeDay(year: number, month: number, day: number) {
    const fday = new Date(year, month - 1, day);
    return !!this.freeDays.find(
      (freeDay) => freeDay.date.getDate() == fday.getDate()
    );
  }

  getMonthName(monthNumber: number): string {
    const date = new Date(this.year, monthNumber - 1);
    return new Intl.DateTimeFormat('en-US', { month: 'long' }).format(date);
  }

  public incrementMonth() {
    if (this.month === 12) {
      this.year++;
      this.month = 1;
      this.ngOnInit();
      return;
    }
    this.month++;
    this.ngOnInit();
  }

  public decrementMonth() {
    if (this.month === 1) {
      this.year--;
      this.month = 12;
      this.ngOnInit();
      return;
    }
    this.month--;
    this.ngOnInit();  
  }

  print() {
    window.print();
  }

  exportExcel(){
    this.dataService.getExcel(this.year).subscribe((response: any)=> {}, (error) => {
        console.error('Error:', error);
        // hardcoded ""temporary"" solution, error gets returned as incorrect type (blob)
        this.errorMsg = "No days planned for this year";
        this.showModal('error');
    });
  }

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService
      .getScheduledDaysByMonth(this.month, this.year)
      .subscribe((response: any[]) => {
        this.data = response;
        this.data.map((item) => (item.date = new Date(item.date)));
      }, (error) => {
        console.error('Error:', error);
        this.errorMsg = error.error;
        this.showModal('error');
      });

    this.dataService
      .getFreeDaysByMonth(this.month, this.year)
      .subscribe((response: any[]) => {
        this.freeDays = response;
        this.freeDays.map((item) => (item.date = new Date(item.date)));
        this.days = this.daysInMonth(this.year, this.month);
      }, (error) => {
        console.error('Error:', error);
        this.errorMsg = error.error;
        this.showModal('error');
      });
  }

  onDragStart(event: DragEvent, draggedObject: Scheduledday) {
    const index = this.data.findIndex((l) => l.id === draggedObject.id);
    event?.dataTransfer?.setData('text', index.toString());
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  onDrop(event: DragEvent, day: Day, classroom: number) {

    const indexOfItemBeingDragged = Number(
      event?.dataTransfer?.getData('text')
    );
    if (typeof indexOfItemBeingDragged !== 'number') {
      return;
    }
    const draggedObject = this.data[indexOfItemBeingDragged];
    if (!day.isFreeDay && !day.isWeekend) {
      
    console.log(this.month);
    console.log(day);
      this.destinationdate = new Date(this.year, this.month-1, day.id+1).toISOString().split('T')[0];
      console.log(this.destinationdate);
      this.destinationclassroom = classroom;
      this.selectedModal = draggedObject.id;
      this.showModal('override-' + draggedObject.id);
    }
    event?.dataTransfer?.clearData?.();
  }

  showModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
      this.selectedModal = null;
    }
  }
}

export interface Scheduledday {
  id: number;
  date: Date;
  classroomId: number;
  groupNumber: number;
  groupColor: string;
  field: string;
  teacher: string;
}

interface Day {
  id: number;
  isWeekend: boolean;
  isFreeDay: boolean;
}

interface FreeDay {
  id: number;
  date: Date;
  name: string;
}
