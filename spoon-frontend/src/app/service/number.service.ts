import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NumberService {
  private cptValue: number;
  random?: Random;

  constructor() {
    this.cptValue = 0;
  }

  incrementCpt(): void {
    this.cptValue++;
  }

  get cpt(): number {
    return this.cptValue;
  }

  nextRandom(): number {
    return this.random?.nextNumber() ?? -1;
  }
}


export interface Random {
  nextNumber(): number;
  nextString(): string;
}

