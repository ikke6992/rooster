import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-override',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './override.component.html',
  styleUrl: './override.component.css',
})
export class OverrideComponent {
  @Input() scheduledday: any;
  @Input() date: string = '';
  @Input() classroomId: number = 0;

  override!: FormGroup;

  constructor(private dataService: DataService) {}

  ngOnInit() {
    this.initializeForm();
  }

  ngOnChanges(changes: any) {
    if (
      (changes.scheduledday && !changes.scheduledday.firstChange) ||
      (changes.date && !changes.date.firstChange) ||
      (changes.classroomId && !changes.classroomId.firstChange)
    ) {
      this.initializeForm();
    }
  }

  private initializeForm() {
    this.override = new FormGroup({
      date: new FormControl(this.date),
      classroomId: new FormControl(this.classroomId),
      adaptWeekly: new FormControl(false),
    });
  }

  onSubmit() {
    const formValue = this.override.value;
    const data = {
      date: formValue.date,
      classroomId: formValue.classroomId,
      adaptWeekly: formValue.adaptWeekly,
    };
    console.log(data);
    this.dataService.override(this.scheduledday.id, data).subscribe(
      (response) => {
        console.log('Response:', response);
        window.location.reload();
      },
      (error) => {
        console.error('Error:', error);
      }
    );
  }
}
