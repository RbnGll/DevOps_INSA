import { browser, by, element } from 'protractor';

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
    browser.actions().click(element(by.tagName('textarea'))).sendKeys(txt).perform();
  }

  countTree(): Promise<string> {
    return element(by.tagName('mat-tree')).getAttribute('value') as Promise<string>;
  }
}
