import {browser, by, element} from 'protractor';

export class AppPage {
  navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl) as Promise<unknown>;
  }

  getTitleText(): Promise<string> {
    return element(by.tagName('h1')).getText() as Promise<string>;
  }

  getTextAreaValue(): Promise<string>{
    return element(by.tagName('textarea')).getAttribute('value') as Promise<string>;
  }

  writeText(txt: string): void {
    browser.findElement(by.tagName('textarea')).clear();
    browser.actions().click(element(by.tagName('textarea'))).sendKeys(txt).perform();
  }

  countTree(): Promise<number> {
    return element(by.tagName('mat-tree')).all(by.tagName('ul')).count() as Promise<number>;
  }

  // getMatTreeValue(): any {
  //   let arrayString = new Array();
  //   element.all(by.className('mat-tree-node')).each(
  //     el => console.log(el.getText())
  //   );
  //   return arrayString;
  // }
}
