import { Component } from '@angular/core';
import { DataService } from './data.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-archive',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './archive.component.html',
  styleUrl: './archive.component.css',
})
export class ArchiveComponent {
  groups: any[] = [];
  BRIGHTNESS_THRESHOLD: number = 128;

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getGroups().subscribe((response: any[]) => {
      this.groups = response;
      console.log(response);
    });
  }

  calculateBrightness(color: string): boolean{
    const r = parseInt(color.substring(1, 3), 16);
    const g = parseInt(color.substring(3, 5), 16);
    const b = parseInt(color.substring(5, 7), 16);

    const brightness = Math.sqrt(0.299*r*r + 0.587*g*g + 0.114*b*b);    
    
    return brightness < this.BRIGHTNESS_THRESHOLD;
  }
}
